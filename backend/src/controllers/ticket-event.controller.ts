import {
  repository,
} from '@loopback/repository';
import {
  param,
  get,
  getModelSchemaRef,
} from '@loopback/rest';
import {
  Ticket,
  Event,
} from '../models';
import {TicketRepository} from '../repositories';

export class TicketEventController {
  constructor(
    @repository(TicketRepository)
    public ticketRepository: TicketRepository,
  ) { }

  @get('/tickets/{id}/event', {
    responses: {
      '200': {
        description: 'Event belonging to Ticket',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Event)},
          },
        },
      },
    },
  })
  async getEvent(
    @param.path.string('id') id: typeof Ticket.prototype.id,
  ): Promise<Event> {
    return this.ticketRepository.Event(id);
  }
}
