import { Entity, model, property, hasOne, hasMany } from '@loopback/repository';
import { UserLocation } from './user-location.model';
import { Ticket } from './ticket.model';
import { UserCredentials } from '@loopback/authentication-jwt';
import { Event } from './event.model';
import { TicketRequest } from './ticket-request.model';

@model()
export class User extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  id: string;

  @property({
    type: 'string',
    required: true,
  })
  firstName: string;

  @property({
    type: 'string',
    required: true,
  })
  lastName: string;

  @property({
    type: 'string',
    required: true,
  })
  email: string;

  @property({
    type: 'string',
    required: true,
  })
  nick_name: string;

  @property({
    type: 'date',
    required: true,
  })
  birth_date: string;

  @property({
    type: 'string',
  })
  profile_pic?: string;

  @property({
    type: 'string',
  })
  about_me?: string;

  @property({
    type: 'string',
    required: true,
  })
  password: string;

  userCredentials: UserCredentials;

  @hasOne(() => UserLocation, { keyTo: 'user_id' })
  userLocation: UserLocation;

  @hasMany(() => Ticket, { keyTo: 'user_id' })
  tickets: Ticket[];

  @hasMany(() => Event, { keyTo: 'user_id' })
  events: Event[];

  @hasMany(() => TicketRequest, { keyTo: 'user_id' })
  ticketRequests: TicketRequest[];

  constructor(data?: Partial<User>) {
    super(data);
  }
}

export interface UserRelations {
  // describe navigational properties here
}

export type UserWithRelations = User & UserRelations;
