import {
  Count,
  CountSchema,
  Filter,
  repository,
  Where,
} from '@loopback/repository';
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
import {
  User,
  UserLocation,
} from '../models';
import {UserRepository} from '../repositories';

export class UserUserLocationController {
  constructor(
    @repository(UserRepository) protected userRepository: UserRepository,
  ) { }

  @get('/users/{id}/user-location', {
    responses: {
      '200': {
        description: 'User has one UserLocation',
        content: {
          'application/json': {
            schema: getModelSchemaRef(UserLocation),
          },
        },
      },
    },
  })
  async get(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<UserLocation>,
  ): Promise<UserLocation> {
    return this.userRepository.userLocation(id).get(filter);
  }

  @post('/users/{id}/user-location', {
    responses: {
      '200': {
        description: 'User model instance',
        content: {'application/json': {schema: getModelSchemaRef(UserLocation)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof User.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(UserLocation, {
            title: 'NewUserLocationInUser',
            exclude: ['user_id'],
            optional: ['user_id']
          }),
        },
      },
    }) userLocation: Omit<UserLocation, 'user_id'>,
  ): Promise<UserLocation> {
    //Deleting the old location before creating a new one
    await this.userRepository.userLocation(id).delete();
    return this.userRepository.userLocation(id).create(userLocation);
  }

  @patch('/users/{id}/user-location', {
    responses: {
      '200': {
        description: 'User.UserLocation PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(UserLocation, {partial: true, exclude: ['user_id']}),
        },
      },
    })
    userLocation: Partial<UserLocation>,
    @param.query.object('where', getWhereSchemaFor(UserLocation)) where?: Where<UserLocation>,
  ): Promise<Count> {
    await this.userRepository.userLocation(id).create(userLocation);
    return this.userRepository.userLocation(id).patch(userLocation, where);
  }

  @del('/users/{id}/user-location', {
    responses: {
      '200': {
        description: 'User.UserLocation DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(UserLocation)) where?: Where<UserLocation>,
  ): Promise<Count> {
    return this.userRepository.userLocation(id).delete(where);
  }
}
