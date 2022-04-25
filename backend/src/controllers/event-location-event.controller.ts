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
  EventLocation,
  Event,
} from '../models';
import {EventLocationRepository} from '../repositories';

@authenticate('jwt')
export class EventLocationEventController {
  constructor(
    @repository(EventLocationRepository)
    public eventLocationRepository: EventLocationRepository,
  ) { }

  @get('/event-locations/{id}/event', {
    responses: {
      '200': {
        description: 'Event belonging to EventLocation',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Event)},
          },
        },
      },
    },
  })
  async getEvent(
    @param.path.string('id') id: typeof EventLocation.prototype.event_id,
  ): Promise<Event> {
    return this.eventLocationRepository.event(id);
  }
}
