// Uncomment these imports to begin using these cool features!
import {
  del,
  get,
  getModelSchemaRef,
  getWhereSchemaFor,
  param,
  patch,
  post,
  requestBody,
} from '@loopback/rest';
import { Filter, repository } from "@loopback/repository";
import { FriendsRepository } from "../repositories";
import { Friends, User } from '../models';

// import {inject} from '@loopback/core';


export class FriendsController {
  constructor(
    @repository(FriendsRepository) protected friendsRepository: FriendsRepository,
  ) { }

  @get('/users/{id}/friends', {
    responses: {
      '200': {
        description: 'Get all friends for a user',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Friends)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Friends>,
  ): Promise<User[]> {
    //TODO query all the friends for that user. 
    return [];
  }
}
