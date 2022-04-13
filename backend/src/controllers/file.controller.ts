// Uncomment these imports to begin using these cool features!

import { authenticate } from "@loopback/authentication";
import { inject } from "@loopback/core";
import { repository } from "@loopback/repository";
import { get, HttpErrors, oas, param, post, Request, requestBody, Response, RestBindings } from "@loopback/rest";
import { SecurityBindings, UserProfile } from "@loopback/security";
import { FILE_UPLOAD_SERVICE } from "../keys";
import { UserRepository } from "../repositories";
import { FileUploadHandler } from "../types";
import path from 'path';
import multer from 'multer';
import { Console } from "console";
import e from "express";
import {STORAGE_DIRECTORY} from '../keys';
import { User } from "@loopback/authentication-jwt";

// import {inject} from '@loopback/core';


export class FileController {
  constructor(
    // @inject(STORAGE_DIRECTORY)
    // private storageDirectory: string,
    @repository(UserRepository)
    public userRepository: UserRepository,
    @inject(FILE_UPLOAD_SERVICE)
    private handler: FileUploadHandler,
  ) { }

  @authenticate('jwt')
  @post('/users/{id}/files', {
    responses: {
      200: {
        content: {
          'application/json': {
            schema: {
              type: 'object',
            },
          },
        },
        description: 'Files and fields',
      },
    },
  })
  async fileUpload(
    @requestBody.file()
    request: Request,
    @inject(RestBindings.Http.RESPONSE) response: Response,
    @param.path.string('id') id: string,
    @inject(SecurityBindings.USER)
    currentUserProfile: UserProfile,
  ): Promise<object> {
    if (id !== currentUserProfile.id) {
      throw new HttpErrors.Unauthorized('You are not authorized to perform this action.');
    }
    let imageUploader = multer(this.getMulterOptions(`u${id}`)).any();
    return new Promise<object>((resolve, reject) => {
      imageUploader(request, response, err => {
        if (err) reject(err);
        else {
          resolve(this.getFilesAndFields(request, id));
        }
      });
    });
  }

  @authenticate('jwt')
  @get('/users/{id}/files')
  @oas.response.file()
  async downloadFile(
    @param.path.string('id') id: string,
    @inject(RestBindings.Http.RESPONSE) response: Response,
    @inject(SecurityBindings.USER)
    currentUserProfile: UserProfile,
  ) {
    let user = await this.userRepository.findById(id);
    // let fileName = `u${id}.png`;
    let fileName = user.profile_pic!;
    const file = this.validateFileName(fileName);
    response.download(file, fileName);
    return response;
  }

  /**
   * Validate file names to prevent them goes beyond the designated directory
   * @param fileName - File name
   */
  private validateFileName(fileName: string) {
    const resolved = path.resolve(STORAGE_DIRECTORY, fileName);
    // return resolved;
    if (resolved.endsWith(fileName)) return resolved;
    // The resolved file is outside sandbox
    throw new HttpErrors.BadRequest(`Invalid file name: ${fileName}`);
  }

  /**
   * Get files and fields for the request
   * @param request - Http request
   */
  private getFilesAndFields(request: Request, user_id: string) {
    const uploadedFiles = request.files;
    let filename = '';
    const mapper = (file: globalThis.Express.Multer.File) => {
      if (!file.mimetype.startsWith('image')) {
        throw new HttpErrors.UnsupportedMediaType(`Unsupported file type: ${file.mimetype}`);
      }
      filename = file.filename;
      return ({
        fieldname: file.fieldname,
        originalname: file.originalname,
        // encoding: f.encoding,
        mimetype: file.mimetype,
        size: file.size,
      })
    };
    let files: object[] = [];
    if (Array.isArray(uploadedFiles)) {
      files = uploadedFiles.map(mapper);
    } else {
      for (const filename in uploadedFiles) {
        files.push(...uploadedFiles[filename].map(mapper));
      }
    }
    //TODO Update the user path!
    this.userRepository.updateById(user_id, {profile_pic: filename})
    return { files, fields: request.body };
  }

  private getMulterOptions(fileName: string): multer.Options {
    return {
      storage: multer.diskStorage({
        destination: STORAGE_DIRECTORY,
        // Use the original file name as is
        filename: (req, file, cb) => {

          //Get the file ending of file.originalname
          const ext = file.originalname.split('.').pop();

          cb(null, `${fileName}.${ext}`);
        },
      })
    };
  }
}
