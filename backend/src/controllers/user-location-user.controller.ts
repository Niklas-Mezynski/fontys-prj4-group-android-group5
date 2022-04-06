import {
  repository,
} from '@loopback/repository';
import {
  param,
  get,
  getModelSchemaRef,
} from '@loopback/rest';
import {
  UserLocation,
  User,
} from '../models';
import {UserLocationRepository} from '../repositories';

export class UserLocationUserController {
  constructor(
    @repository(UserLocationRepository)
    public userLocationRepository: UserLocationRepository,
  ) { }

  @get('/user-locations/{id}/user', {
    responses: {
      '200': {
        description: 'User belonging to UserLocation',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(User)},
          },
        },
      },
    },
  })
  async getUser(
    @param.path.string('id') id: typeof UserLocation.prototype.user_id,
  ): Promise<User> {
    return this.userLocationRepository.user(id);
  }
}
