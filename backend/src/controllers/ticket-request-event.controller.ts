import {
  repository,
} from '@loopback/repository';
import {
  param,
  get,
  getModelSchemaRef,
} from '@loopback/rest';
import {
  TicketRequest,
  Event,
} from '../models';
import {TicketRequestRepository} from '../repositories';

export class TicketRequestEventController {
  constructor(
    @repository(TicketRequestRepository)
    public ticketRequestRepository: TicketRequestRepository,
  ) { }

  @get('/ticket-requests/{id}/event', {
    responses: {
      '200': {
        description: 'Event belonging to TicketRequest',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(Event)},
          },
        },
      },
    },
  })
  async getEvent(
    @param.path.string('id') id: typeof TicketRequest.prototype.event_id,
  ): Promise<Event> {
    return this.ticketRequestRepository.TicketRequestToEvent(id);
  }
}
