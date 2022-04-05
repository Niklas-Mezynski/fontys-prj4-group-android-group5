import {Entity, model, property} from '@loopback/repository';

@model()
export class Pictures extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  event_id: string;

  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  url: string;


  constructor(data?: Partial<Pictures>) {
    super(data);
  }
}

export interface PicturesRelations {
  // describe navigational properties here
}

export type PicturesWithRelations = Pictures & PicturesRelations;
