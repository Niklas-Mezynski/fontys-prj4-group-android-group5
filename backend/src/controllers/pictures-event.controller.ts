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
  Pictures,
  Event,
} from '../models';
import {PicturesRepository} from '../repositories';

@authenticate('jwt')
export class PicturesEventController {
  constructor(
    @repository(PicturesRepository)
    public picturesRepository: PicturesRepository,
  ) { }

  @get('/pictures/{id}/event', {
    responses: {
      '200': {
        description: 'Event belonging to Pictures',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Event)},
          },
        },
      },
    },
  })
  async getEvent(
    @param.path.string('id') id: typeof Pictures.prototype.event_id,
  ): Promise<Event> {
    return this.picturesRepository.event(id);
  }
}
