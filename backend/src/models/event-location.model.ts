import {Entity, model, property} from '@loopback/repository';

@model()
export class EventLocation extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  event_id: string;

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


  constructor(data?: Partial<EventLocation>) {
    super(data);
  }
}

export interface EventLocationRelations {
  // describe navigational properties here
}

export type EventLocationWithRelations = EventLocation & EventLocationRelations;
