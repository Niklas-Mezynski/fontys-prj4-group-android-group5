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
  User,
} from '../models';
import {TicketRequestRepository} from '../repositories';

export class TicketRequestUserController {
  constructor(
    @repository(TicketRequestRepository)
    public ticketRequestRepository: TicketRequestRepository,
  ) { }

  @get('/ticket-requests/{id}/user', {
    responses: {
      '200': {
        description: 'User belonging to TicketRequest',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(User)},
          },
        },
      },
    },
  })
  async getUser(
    @param.path.string('id') id: typeof TicketRequest.prototype.user_id,
  ): Promise<User> {
    return this.ticketRequestRepository.TicketRequestToUser(id);
  }
}
