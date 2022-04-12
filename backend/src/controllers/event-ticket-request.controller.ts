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
  TicketRequest,
} from '../models';
import {EventRepository} from '../repositories';

export class EventTicketRequestController {
  constructor(
    @repository(EventRepository) protected eventRepository: EventRepository,
  ) { }

  @get('/events/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'Array of Event has many TicketRequest',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(TicketRequest)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<TicketRequest>,
  ): Promise<TicketRequest[]> {
    return this.eventRepository.ticketRequests(id).find(filter);
  }

  @post('/events/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'Event model instance',
        content: {'application/json': {schema: getModelSchemaRef(TicketRequest)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Event.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {
            title: 'NewTicketRequestInEvent',
            exclude: ['event_id'],
            optional: ['event_id']
          }),
        },
      },
    }) ticketRequest: Omit<TicketRequest, 'event_id'>,
  ): Promise<TicketRequest> {
    return this.eventRepository.ticketRequests(id).create(ticketRequest);
  }

  @patch('/events/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'Event.TicketRequest PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {partial: true}),
        },
      },
    })
    ticketRequest: Partial<TicketRequest>,
    @param.query.object('where', getWhereSchemaFor(TicketRequest)) where?: Where<TicketRequest>,
  ): Promise<Count> {
    return this.eventRepository.ticketRequests(id).patch(ticketRequest, where);
  }

  @del('/events/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'Event.TicketRequest DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(TicketRequest)) where?: Where<TicketRequest>,
  ): Promise<Count> {
    return this.eventRepository.ticketRequests(id).delete(where);
  }
}
