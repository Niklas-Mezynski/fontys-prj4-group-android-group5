import { authenticate } from '@loopback/authentication';
import {
  repository,
} from '@loopback/repository';
import {
  param,
  get,
  getModelSchemaRef,
} from '@loopback/rest';
import {
  Event,
  User,
} from '../models';
import {EventRepository} from '../repositories';

@authenticate('jwt')
export class EventUserController {
  constructor(
    @repository(EventRepository)
    public eventRepository: EventRepository,
  ) { }

  @get('/events/{id}/user', {
    responses: {
      '200': {
        description: 'User belonging to Event',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(User)},
          },
        },
      },
    },
  })
  async getUser(
    @param.path.string('id') id: typeof Event.prototype.id,
  ): Promise<User> {
    return this.eventRepository.EventUser(id);
  }
}
