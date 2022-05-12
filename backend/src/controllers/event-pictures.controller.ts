import { authenticate } from '@loopback/authentication';
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
import { Helpers } from '../helpers/helper_functions';
import {
  Event,
  Pictures,
} from '../models';
import {EventRepository} from '../repositories';

@authenticate('jwt')
export class EventPicturesController {
  constructor(
    @repository(EventRepository) protected eventRepository: EventRepository,
  ) { }

  @get('/events/{id}/pictures', {
    responses: {
      '200': {
        description: 'Array of Event has many Pictures',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Pictures)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Pictures>,
  ): Promise<Pictures[]> {
    return this.eventRepository.pictures(id).find(filter);
  }

  @post('/events/{id}/pictures', {
    responses: {
      '200': {
        description: 'Event model instance',
        content: {'application/json': {schema: getModelSchemaRef(Pictures)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Event.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Pictures, {
            title: 'NewPicturesInEvent',
            exclude: ['event_id', 'img_uuid'],
            optional: ['event_id']
          }),
        },
      },
    }) pictures: Pictures,
  ): Promise<Pictures> {
    pictures.img_uuid = Helpers.generateUUID();
    return this.eventRepository.pictures(id).create(pictures);
  }
  
  @patch('/events/{id}/pictures', {
    responses: {
      '200': {
        description: 'Event.Pictures PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Pictures, {partial: true}),
        },
      },
    })
    pictures: Partial<Pictures>,
    @param.query.object('where', getWhereSchemaFor(Pictures)) where?: Where<Pictures>,
  ): Promise<Count> {
    return this.eventRepository.pictures(id).patch(pictures, where);
  }

  @del('/events/{id}/pictures', {
    responses: {
      '200': {
        description: 'Event.Pictures DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(Pictures)) where?: Where<Pictures>,
  ): Promise<Count> {
    return this.eventRepository.pictures(id).delete(where);
  }
}
