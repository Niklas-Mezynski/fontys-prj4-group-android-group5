import { authenticate } from '@loopback/authentication';
import {
  Count,
  CountSchema,
  Filter,
  FilterExcludingWhere,
  repository,
  Where,
  WhereBuilder,
} from '@loopback/repository';
import {
  post,
  param,
  get,
  getModelSchemaRef,
  patch,
  put,
  del,
  requestBody,
  response, getWhereSchemaFor, HttpErrors,
} from '@loopback/rest';
import { getMessaging } from 'firebase-admin/messaging';
import { Helpers } from '../helpers/helper_functions';
import { Ticket, TicketRequest } from '../models';
import { TicketRequestRepository, EventRepository, UserRepository, TicketRepository } from '../repositories';

@authenticate('jwt')
export class TicketRequestController {
  constructor(
    @repository(TicketRequestRepository)
    protected ticketRequestRepository: TicketRequestRepository,
    @repository(EventRepository)
    protected eventRepository: EventRepository,
    @repository(UserRepository)
    protected userRepository: UserRepository,
    @repository(TicketRepository)
    protected ticketRepository: TicketRepository,
  ) { }

  @post('/ticket-requests')
  @response(200, {
    description: 'TicketRequest model instance',
    content: { 'application/json': { schema: getModelSchemaRef(TicketRequest) } },
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {
            title: 'NewTicketRequest',
          }),
        },
      },
    })
    ticketRequest: TicketRequest,
  ): Promise<TicketRequest> {
    let newRequest = await this.ticketRequestRepository.create(ticketRequest);
    this.newTicketRequestMessage(ticketRequest);
    return newRequest;
  }

  async newTicketRequestMessage(ticketRequest: TicketRequest) {

    //Get the event creator (and his device token)
    let event = await this.eventRepository.findById(ticketRequest.event_id);
    let event_creator = await this.userRepository.findById(event.user_id);
    if (!event_creator.firebaseToken) {
      return;
    }
    const registrationToken = event_creator.firebaseToken;

    let requestingUser = await this.userRepository.findById(ticketRequest.user_id);
    //Send the message
    const message = {
      notification: {
        title: 'New join request!',
        body: `'${requestingUser.nick_name}' asks to join your party.`
      },
      token: registrationToken
    };

    //Sending the message
    getMessaging().send(message);
  }

  @get('/ticket-requests/count')
  @response(200, {
    description: 'TicketRequest model count',
    content: { 'application/json': { schema: CountSchema } },
  })
  async count(
    @param.where(TicketRequest) where?: Where<TicketRequest>,
  ): Promise<Count> {
    return this.ticketRequestRepository.count(where);
  }

  @get('/ticket-requests')
  @response(200, {
    description: 'Array of TicketRequest model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(TicketRequest, { includeRelations: true }),
        },
      },
    },
  })
  async find(
    @param.filter(TicketRequest) filter?: Filter<TicketRequest>,
    // @param.query.object('filter') filter?: Filter<TicketRequest>,
  ): Promise<TicketRequest[]> {
    return this.ticketRequestRepository.find(filter);
  }

  /*@patch('/ticket-requests')
  @response(200, {
    description: 'TicketRequest PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {partial: true}),
        },
      },
    })
    ticketRequest: TicketRequest,
    @param.where(TicketRequest) where?: Where<TicketRequest>,
  ): Promise<Count> {
    return this.ticketRequestRepository.updateAll(ticketRequest, where);
  }*/

  @get('/ticket-requests/event/{id}', {
    responses: {
      '200': {
        description: 'Array of TicketRequest model instances by Event-ID',
        content: {
          'application/json': {
            schema: {
              type: 'array',
              items: getModelSchemaRef(TicketRequest)
            },
          },
        },
      },
    },
  })
  async findByEventId(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<TicketRequest>,
  ): Promise<TicketRequest[]> {
    return this.eventRepository.ticketRequests(id).find(filter);
  }

  @get('/ticket-requests/user/{id}', {
    responses: {
      '200': {
        description: 'Array of TicketRequest model instances by User-ID',
        content: {
          'application/json': {
            schema: {
              type: 'array',
              items: getModelSchemaRef(TicketRequest)
            },
          },
        },
      },
    },
  })
  async findByUserId(
    @param.path.string('id') id: string,
    @param.query.object('filter') filter?: Filter<TicketRequest>,
  ): Promise<TicketRequest[]> {
    return this.userRepository.ticketRequests(id).find(filter);
  }

  /*@patch('/ticket-requests/{id}')
  @response(204, {
    description: 'TicketRequest PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(TicketRequest, {partial: true}),
        },
      },
    })
    ticketRequest: TicketRequest,
  ): Promise<void> {
    await this.ticketRequestRepository.updateById(id, ticketRequest);
  }

  @put('/ticket-requests/{id}')
  @response(204, {
    description: 'TicketRequest PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() ticketRequest: TicketRequest,
  ): Promise<void> {
    await this.ticketRequestRepository.replaceById(id, ticketRequest);
  }*/

  @del('/ticket-requests')
  @response(204, {
    description: 'TicketRequest DELETE success',
  })
  async delete(
    @param.query.object('where', getWhereSchemaFor(TicketRequest)) where?: Where<TicketRequest>,
  ): Promise<number> {
    return (await this.ticketRequestRepository.deleteAll(where)).count
  }

  @post('/ticket-requests/accept/{event_id},{user_id}')
  @response(200, {
    description: 'TicketRequest model instance',
    content: { 'application/json': { schema: getModelSchemaRef(TicketRequest) } },
  })
  async acceptRequest(
    @param.path.string('event_id') r_event_id: string,
    @param.path.string('user_id') r_user_id: string,
  ): Promise<Ticket> {



    //Delete the request
    let where = new WhereBuilder<TicketRequest>()
      .and({ event_id: r_event_id }, { user_id: r_user_id })
      .build();
    let count = (await this.ticketRequestRepository.deleteAll(where)).count;
    if (!count) {
      throw new HttpErrors[404]("Ticket request not found");
    }

    //Create the ticket
    let ticket = await this.ticketRepository.create({
      event_id: r_event_id,
      user_id: r_user_id,
      id: Helpers.generateUUID()
    });

    this.sendAcceptMessage(ticket);

    return ticket;
  }

  async sendAcceptMessage(ticket: Ticket) {    
    //Get the ticket requester creator (and his device token) and the event infos
    let event = await this.eventRepository.findById(ticket.event_id);
    let request_user = await this.userRepository.findById(ticket.user_id);
    if (!request_user.firebaseToken) {
      return;
    }
    const registrationToken = request_user.firebaseToken;

    //Send the message
    const message = {
      notification: {
        title: 'Ticket request accepted!',
        body: `Your request for the party '${event.name}' was accepted.`
      },
      token: registrationToken
    };

    //Sending the message
    getMessaging().send(message);
  }
  

}