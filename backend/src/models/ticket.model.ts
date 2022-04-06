import {Entity, model, property, belongsTo} from '@loopback/repository';
import {User} from './user.model';
import {Event} from './event.model';

@model()
export class Ticket extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  id: string;
  @belongsTo(() => User, {name: 'User'})
  user_id: string;

  @belongsTo(() => Event, {name: 'Event'})
  event_id: string;

  constructor(data?: Partial<Ticket>) {
    super(data);
  }
}

export interface TicketRelations {
  // describe navigational properties here
}

export type TicketWithRelations = Ticket & TicketRelations;
