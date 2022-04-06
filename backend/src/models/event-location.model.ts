import { Entity, model, property, belongsTo } from '@loopback/repository';
import { Event } from './event.model';

@model()
export class EventLocation extends Entity {
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
    type: 'date',
    required: true,
  })
  created_on: string;

  @belongsTo(() => Event, { name: 'event' }, {
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  event_id: string;

  constructor(data?: Partial<EventLocation>) {
    super(data);
  }
}

export interface EventLocationRelations {
  // describe navigational properties here
}

export type EventLocationWithRelations = EventLocation & EventLocationRelations;
