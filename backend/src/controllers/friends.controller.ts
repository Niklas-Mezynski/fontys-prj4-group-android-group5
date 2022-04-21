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
import { AnyObject, Filter, repository } from "@loopback/repository";
import { FriendsRepository } from "../repositories";
import { Friends, User } from '../models';
import { authenticate } from '@loopback/authentication';

// import {inject} from '@loopback/core';

@authenticate('jwt')
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
            // schema: {type: 'array', items: getModelSchemaRef(Friends)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Friends>,
  ): Promise<AnyObject> {
    //TODO query all the friends for that user.
    // let sql: string = `SELECT "user".nick_name FROM ((SELECT f.usera as friendId FROM friends f where (userb = '${id}')) UNION (SELECT f.userb as friendId FROM friends f where (usera = '${id}'))) AS friends INNER JOIN "user" ON "user".id = friends.friendId;`;
    let sql: string = `SELECT * FROM getFriendInfos('${id}');`;
    let queryResult = await this.friendsRepository.execute(sql);
    return queryResult;
  }
}
