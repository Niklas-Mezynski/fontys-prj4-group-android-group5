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
import { UserRepository } from '../repositories';
import { authenticate, TokenService } from '@loopback/authentication';
import { inject } from '@loopback/core';
import {
  Credentials,
  MyUserService,
  TokenServiceBindings,
  UserServiceBindings,
} from '@loopback/authentication-jwt';
import { SecurityBindings, securityId, UserProfile } from '@loopback/security';
import { Console } from 'console';


export class UserTicketController {
  constructor(
    @repository(UserRepository) protected userRepository: UserRepository,
  ) { }

  @authenticate('jwt') // Add this annotation in order to secure it using JWT
  @get('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'Array of User has many Ticket',
        content: {
          'application/json': {
            schema: { type: 'array', items: getModelSchemaRef(Ticket) },
          },
        },
      },
    },
  })
  async find(
    @param.path.string('id') id: string,
    @inject(SecurityBindings.USER)   // Add these two lines to get the user profile by the JWT token in the header
    currentUserProfile: UserProfile, //
    @param.query.object('filter') filter?: Filter<Ticket>,
  ): Promise<Ticket[]> {
    //In this case: verify that the requesting user is equal to the user_id in the request
    //Maybe do other verifications here
    if (currentUserProfile[securityId] != id) {
      throw new Error("You don't have permission to access this resource");
    }
    return this.userRepository.tickets(id).find(filter);
  }

  @post('/users/{id}/tickets', {
    responses: {
      '200': {
        description: 'User model instance',
        content: { 'application/json': { schema: getModelSchemaRef(Ticket) } },
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
        content: { 'application/json': { schema: CountSchema } },
      },
    },
  })
  async patch(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Ticket, { partial: true }),
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
        content: { 'application/json': { schema: CountSchema } },
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
