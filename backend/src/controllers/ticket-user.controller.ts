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
  Ticket,
  User,
} from '../models';
import {TicketRepository} from '../repositories';

@authenticate('jwt')
export class TicketUserController {
  constructor(
    @repository(TicketRepository)
    public ticketRepository: TicketRepository,
  ) { }

  @get('/tickets/{id}/user', {
    responses: {
      '200': {
        description: 'User belonging to Ticket',
        content: {
          'application/json': {
            schema: {type: 'array', items: getModelSchemaRef(User)},
          },
        },
      },
    },
  })
  async getUser(
    @param.path.string('id') id: typeof Ticket.prototype.id,
  ): Promise<User> {
    return this.ticketRepository.User(id);
  }
}
