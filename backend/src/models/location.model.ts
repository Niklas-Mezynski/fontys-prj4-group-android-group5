import {Entity, model, property} from '@loopback/repository';

@model()
export class Location extends Entity {
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


  constructor(data?: Partial<Location>) {
    super(data);
  }
}

export interface LocationRelations {
  // describe navigational properties here
}

export type LocationWithRelations = Location & LocationRelations;
