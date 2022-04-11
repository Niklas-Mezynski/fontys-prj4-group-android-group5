import {Entity, model, property, hasMany, hasOne, belongsTo} from '@loopback/repository';
import {Pictures} from './pictures.model';
import {EventLocation} from './event-location.model';
import {Ticket} from './ticket.model';
import {User} from './user.model';

@model()
export class Event extends Entity {
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
  name: string;

  @property({
    type: 'string',
    required: true,
  })
  description: string;

  @property({
    type: 'date',
    required: true,
  })
  start: string;

  @property({
    type: 'date',
  })
  end?: string;

  @property({
    type: 'number',
    required: true,
  })
  max_people: number;

  @hasMany(() => Pictures, {keyTo: 'event_id'})
  pictures: Pictures[];

  @hasOne(() => EventLocation, {keyTo: 'event_id'})
  eventLocation: EventLocation;

  @hasMany(() => Ticket, {keyTo: 'event_id'})
  tickets: Ticket[];

  @belongsTo(() => User, {name: 'EventUser'})
  user_id: string;

  constructor(data?: Partial<Event>) {
    super(data);
  }
}

export interface EventRelations {
  // describe navigational properties here
}

export type EventWithRelations = Event & EventRelations;
