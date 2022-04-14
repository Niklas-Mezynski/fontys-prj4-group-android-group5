import {
  Count,
  CountSchema,
  Filter,
  FilterExcludingWhere,
  repository,
  Where,
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
  response, getWhereSchemaFor,
} from '@loopback/rest';
import {TicketRequest} from '../models';
import {TicketRequestRepository, EventRepository, UserRepository} from '../repositories';
import {inject} from '@loopback/core';

export class TicketRequestController {
  constructor(
    @repository(TicketRequestRepository)
    protected ticketRequestRepository : TicketRequestRepository,
	@repository(EventRepository)
	protected eventRepository: EventRepository,
	@repository(UserRepository)
	protected userRepository: UserRepository,
  ) {}

  @post('/ticket-requests')
  @response(200, {
    description: 'TicketRequest model instance',
    content: {'application/json': {schema: getModelSchemaRef(TicketRequest)}},
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
    return this.ticketRequestRepository.create(ticketRequest);
  }

  @get('/ticket-requests/count')
  @response(200, {
    description: 'TicketRequest model count',
    content: {'application/json': {schema: CountSchema}},
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
}
