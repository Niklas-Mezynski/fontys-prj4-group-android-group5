import {Entity, model, property, hasMany} from '@loopback/repository';
import {Pictures} from './pictures.model';

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
  location: string;

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

  constructor(data?: Partial<Event>) {
    super(data);
  }
}

export interface EventRelations {
  // describe navigational properties here
}

export type EventWithRelations = Event & EventRelations;
