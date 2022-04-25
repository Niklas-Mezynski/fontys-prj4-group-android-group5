import {belongsTo, Model, model, property} from '@loopback/repository';
import {User} from './user.model';

@model()
export class EventWithLocation extends Model {
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
  })
  created_on?: string;

  @property({
    type: 'string',
    required: true,
  })
  user_id: string;


  constructor(data?: Partial<EventWithLocation>) {
    super(data);
  }
}

export interface EventWithLocationRelations {
  // describe navigational properties here
}

export type EventWithLocationWithRelations = EventWithLocation & EventWithLocationRelations;
