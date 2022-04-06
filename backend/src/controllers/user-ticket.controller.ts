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
  Ticket,
} from '../models';
import {UserRepository} from '../repositories';

export class UserTicketController {
  constructor(
    @repository(UserRepository) protected userRepository: UserRepository,
  ) { }

  @get('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'Array of User has many Ticket',
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
    return this.userRepository.tickets(id).find(filter);
  }

  @post('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'User model instance',
        content: {'application/json': {schema: getModelSchemaRef(Ticket)}},
      },
    },
  })
  async create(
    @param.path.string('id') id: typeof User.prototype.id,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Ticket, {
            title: 'NewTicketInUser',
            exclude: ['id'],
            optional: ['user_id']
          }),
        },
      },
    }) ticket: Omit<Ticket, 'id'>,
  ): Promise<Ticket> {
    return this.userRepository.tickets(id).create(ticket);
  }

  @patch('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'User.Ticket PATCH success count',
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
    return this.userRepository.tickets(id).patch(ticket, where);
  }

  @del('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'User.Ticket DELETE success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async delete(
    @param.path.string('id') id: string,
    @param.query.object('where', getWhereSchemaFor(Ticket)) where?: Where<Ticket>,
  ): Promise<Count> {
    return this.userRepository.tickets(id).delete(where);
  }
}
