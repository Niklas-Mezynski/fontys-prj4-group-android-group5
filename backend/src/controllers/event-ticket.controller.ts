import { authenticate } from '@loopback/authentication';
import {
  AnyObject,
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
  response,
} from '@loopback/rest';
import { count } from 'console';
import {
  Event,
  Ticket,
} from '../models';
import {EventRepository} from '../repositories';

@authenticate('jwt')
export class EventTicketController {
  constructor(
    @repository(EventRepository) protected eventRepository: EventRepository,
  ) { }

  @get('/events/{id}/tickets', {
    responses: {
      '200': {
        description: 'Array of Event has many Ticket',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Ticket)},
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<Ticket>,
  ): Promise<Ticket[]> {
    return this.eventRepository.tickets(id).find(filter);
  }

  @post('/events/{id}/tickets', {
    responses: {
      '200': {
        description: 'Event model instance',
        content: {'application/json': {schema: getModelSchemaRef(Ticket)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof Event.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Ticket, {
            title: 'NewTicketInEvent',
            exclude: ['id'],
            optional: ['event_id']
          }),
        },
      },
    }) ticket: Omit<Ticket, 'id'>,
  ): Promise<Ticket> {
    return this.eventRepository.tickets(id).create(ticket);
  }

  @patch('/events/{id}/tickets', {
    responses: {
      '200': {
        description: 'Event.Ticket PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Ticket, {partial: true}),
        },
      },
    })
    ticket: Partial<Ticket>,
    @param.query.object('where', getWhereSchemaFor(Ticket)) where?: Where<Ticket>,
  ): Promise<Count> {
    return this.eventRepository.tickets(id).patch(ticket, where);
  }

  @del('/events/{id}/tickets', {
    responses: {
      '200': {
        description: 'Event.Ticket DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(Ticket)) where?: Where<Ticket>,
  ): Promise<Count> {
    return this.eventRepository.tickets(id).delete(where);
  }

  @get('/events/{id}/tickets/count')
  @response(200, {
    description: 'Tickets for Event count',
    content: { 'application/json': { schema: CountSchema } },
  })
  async count(
    @param.path.string('id') id: string,
  ): Promise<AnyObject> {
    return this.eventRepository.execute("select COUNT(*) from ticket where event_id = '"+id+"'");
  }
}
