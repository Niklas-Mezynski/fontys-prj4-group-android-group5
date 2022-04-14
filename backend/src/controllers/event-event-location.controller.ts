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
  Event,
  EventLocation,
} from '../models';
import {EventRepository} from '../repositories';

export class EventEventLocationController {
  constructor(
    @repository(EventRepository) protected eventRepository: EventRepository,
  ) { }

  @get('/events/{id}/event-location', {
    responses: {
      '200': {
        description: 'Event has one EventLocation',
        content: {
          'application/json': {
            schema: getModelSchemaRef(EventLocation),
          },
        },
      },
    },
  })
  async get(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<EventLocation>,
  ): Promise<EventLocation> {
    return this.eventRepository.eventLocation(id).get(filter);
  }

  @post('/events/{id}/event-location', {
    responses: {
      '200': {
        description: 'Event model instance',
        content: {'application/json': {schema: getModelSchemaRef(EventLocation)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Event.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(EventLocation, {
            title: 'NewEventLocationInEvent',
            optional: ['event_id']
          }),
        },
      },
    }) eventLocation: Omit<EventLocation, 'event_id'>,
  ): Promise<EventLocation> {
    return this.eventRepository.eventLocation(id).create(eventLocation);
  }

  @patch('/events/{id}/event-location', {
    responses: {
      '200': {
        description: 'Event.EventLocation PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(EventLocation, {partial: true}),
        },
      },
    })
    eventLocation: Partial<EventLocation>,
    @param.query.object('where', getWhereSchemaFor(EventLocation)) where?: Where<EventLocation>,
  ): Promise<Count> {
    return this.eventRepository.eventLocation(id).patch(eventLocation, where);
  }

  @del('/events/{id}/event-location', {
    responses: {
      '200': {
        description: 'Event.EventLocation DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(EventLocation)) where?: Where<EventLocation>,
  ): Promise<Count> {
    return this.eventRepository.eventLocation(id).delete(where);
  }
}
