import { authenticate } from '@loopback/authentication';
import { inject } from '@loopback/core';
import {
  AnyObject,
  Count,
  CountSchema,
  DefaultTransactionalRepository,
  Filter,
  FilterExcludingWhere,
  IsolationLevel,
  model,
  property,
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
  response,
} from '@loopback/rest';
import { Helpers } from '../helpers/helper_functions';
import { Event, EventLocation, EventWithRelations, EventWithLocation as EventWithALocation } from '../models';
import { EventRepository } from '../repositories';
import { SecurityBindings, securityId, UserProfile } from "@loopback/security";


@model()
export class EventWithLocation extends Event {
  @property({
    type: 'number',
    required: true,
  })
  latitude: number;

  @property({
    type: 'number',
    required: true,
  })
  longitude: number;

  @property({
    type: 'string',
    required: true,
  })
  event_id: string;
}

@authenticate('jwt')
export class EventController {
  constructor(
    @repository(EventRepository)
    public eventRepository: EventRepository,
  ) { }

  @post('/events')
  @response(200, {
    description: 'Event model instance',
    content: { 'application/json': { schema: getModelSchemaRef(Event) } },
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Event, {
            title: 'NewEvent',
            exclude: ['id'],
          }),
        },
      },
    })
    event: Event,
  ): Promise<Event> {
    event.id = Helpers.generateUUID();
    return this.eventRepository.create(event);
  }






  @post('/event-with-location')
  @response(200, {
    description: 'Success',
    content: { 'application/json': { schema: getModelSchemaRef(Event) } },
  })
  async createEventWithLocation(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(EventWithALocation, {
            title: 'NewEventWithALocation',
            exclude: ['id',],
          }),
        },
      },
    })
    event: EventWithALocation,
  ): Promise<Event> {
    const repo1 = new DefaultTransactionalRepository(Event, this.eventRepository.dataSource);
    const repo2 = new DefaultTransactionalRepository(EventLocation, this.eventRepository.dataSource);

    const tx = await repo2.beginTransaction(IsolationLevel.READ_COMMITTED);

    event.id = Helpers.generateUUID();
    let e = new Event({
      id: event.id,
      name: event.name,
      description: event.description,
      start: event.start,
      max_people: event.max_people,
      user_id: event.user_id
    })
    if (event.end !== null) {
      e.end = event.end;
    }
    const createdEvent = await repo1.create(e, { transaction: tx });

    let timestamp = new Date().toDateString();
    console.log(timestamp);
    const location = new EventLocation({
      event_id: event.id,
      latitude: event.latitude,
      longitude: event.longitude,
      created_on: timestamp,
    });


    await repo2.create(location, { transaction: tx });

    await tx.commit();

    return createdEvent;
  }






  @get('/events/count')
  @response(200, {
    description: 'Event model count',
    content: { 'application/json': { schema: CountSchema } },
  })
  async count(
    @param.where(Event) where?: Where<Event>,
  ): Promise<Count> {
    return this.eventRepository.count(where);
  }

  @get('/events')
  @response(200, {
    description: 'Array of Event model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(Event, { includeRelations: true }),
        },
      },
    },
  })
  async find(
    @param.filter(Event) filter?: Filter<Event>,
  ): Promise<Event[]> {
    return this.eventRepository.find(filter);
  }

  @patch('/events')
  @response(200, {
    description: 'Event PATCH success count',
    content: { 'application/json': { schema: CountSchema } },
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Event, { partial: true }),
        },
      },
    })
    event: Event,
    @param.where(Event) where?: Where<Event>,
  ): Promise<Count> {
    return this.eventRepository.updateAll(event, where);
  }

  @get('/events/{id}')
  @response(200, {
    description: 'Event model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(Event, { includeRelations: true }),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(Event, { exclude: 'where' }) filter?: FilterExcludingWhere<Event>
  ): Promise<EventWithRelations> {
    return this.eventRepository.findById(id, filter);
  }

  @patch('/events/{id}')
  @response(204, {
    description: 'Event PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Event, { partial: true }),
        },
      },
    })
    event: Event,
  ): Promise<void> {
    await this.eventRepository.updateById(id, event);
  }

  @put('/events/{id}')
  @response(204, {
    description: 'Event PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() event: Event,
  ): Promise<void> {
    await this.eventRepository.replaceById(id, event);
  }

  @del('/events/{id}')
  @response(204, {
    description: 'Event DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.eventRepository.deleteById(id);
  }


  @get('/eventsByLocationRadius/{lat},{lon},{radius}')
  @response(200, {
    description: 'Event model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(EventWithLocation, { includeRelations: true }),
      },
    },
  })
  async findByLocation(
    @param.path.number('lat') lat: number,
    @param.path.number('lon') lon: number,
    @param.path.number('radius') radius: number,
    @inject(SecurityBindings.USER)
    currentUserProfile: UserProfile,
    @param.filter(Event, { exclude: 'where' }) filter?: FilterExcludingWhere<Event>
  ): Promise<AnyObject> {
    //Calling a custom SQL function to get the nearby parties
    let sql: string = `SELECT * FROM getNearbyEvents(${lat}, ${lon}, ${radius}, '${currentUserProfile[securityId]}');`;
    return this.eventRepository.execute(sql);
  }
}

