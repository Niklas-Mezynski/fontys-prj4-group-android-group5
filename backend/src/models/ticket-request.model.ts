import {belongsTo, Entity, model, property} from '@loopback/repository';
import { User } from './user.model';
import { Event } from './event.model';

@model()
export class TicketRequest extends Entity {
  @property({
    type: 'date',
    required: true,
  })
  created_on: string;

  @belongsTo(() => User, {name: 'TicketRequestToUser'}, {
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  user_id: string;

  @belongsTo(() => Event, {name: 'TicketRequestToEvent'}, {
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  event_id: string;

  constructor(data?: Partial<TicketRequest>) {
    super(data);
  }
}

export interface TicketRequestRelations {
  // describe navigational properties here
}

export type TicketRequestWithRelations = TicketRequest & TicketRequestRelations;
