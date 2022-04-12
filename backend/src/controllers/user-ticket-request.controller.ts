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
  User,
  TicketRequest,
} from '../models';
import {UserRepository} from '../repositories';

export class UserTicketRequestController {
  constructor(
    @repository(UserRepository) protected userRepository: UserRepository,
  ) { }

  @get('/users/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'Array of User has many TicketRequest',
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
    return this.userRepository.ticketRequests(id).find(filter);
  }

  @post('/users/{userId}/ticket-requests', {
    responses: {
      '200': {
        description: 'User model instance',
        content: {'application/json': {schema: getModelSchemaRef(TicketRequest)}},
      },
    },
  })
  async create(
    @param.path.string('userId') userId: typeof User.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {
            title: 'NewTicketRequestInUser',
			exclude: ['user_id']
          }),
        },
      },
    }) ticketRequest: TicketRequest,
  ): Promise<TicketRequest> {
    return this.userRepository.ticketRequests(userId).create(ticketRequest);
  }

  @patch('/users/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'User.TicketRequest PATCH success count',
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
    return this.userRepository.ticketRequests(id).patch(ticketRequest, where);
  }

  @del('/users/{id}/ticket-requests', {
    responses: {
      '200': {
        description: 'User.TicketRequest DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(TicketRequest)) where?: Where<TicketRequest>,
  ): Promise<Count> {
    return this.userRepository.ticketRequests(id).delete(where);
  }
}
