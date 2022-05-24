// Uncomment these imports to begin using these cool features!
import {
  del,
  get,
  param,
  response,
} from '@loopback/rest';
import {AnyObject, Filter, repository} from '@loopback/repository';
import { FriendsRepository } from "../repositories";
import {Friends} from '../models';
import { authenticate } from '@loopback/authentication';
import {inject} from '@loopback/core';
import {SecurityBindings, UserProfile} from '@loopback/security';

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
    // let sql: string = `SELECT * FROM getFriendInfos('${id}');`;
    // let queryResult = await this.friendsRepository.execute(sql);
    const queryResult = await this.friendsRepository.getFriendsForUser(id);
    return queryResult;
  }

  @get('/users/search/{name}')
  @response(200, {
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
  async findByName(
    @param.path.string('name') username: string,
    @param.query.object('filter') filter?: Filter<Friends>,
  ): Promise<AnyObject> {
    const friendInfo =await this.friendsRepository.getFriendByName(username);

    return friendInfo[0];
  }

  @del("/users/{user_id}/friends/{friend_id}")
  @response(204, {
    description: 'Event DELETE success',
  })
  async deleteByIds(
    @param.path.string('user_id') userId: string,
    @param.path.string('friend_id') friendId: string,
    @inject(SecurityBindings.USER) currentUserProfile: UserProfile
  ): Promise<void> {
    await this.friendsRepository.deleteByIds(userId, friendId, currentUserProfile);
  }

}
