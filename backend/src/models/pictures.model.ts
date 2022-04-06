import {Entity, model, property, belongsTo} from '@loopback/repository';
import {Event} from './event.model';

@model()
export class Pictures extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  url: string;

  @belongsTo(() => Event, {name: 'event'})
  event_id: string;

  constructor(data?: Partial<Pictures>) {
    super(data);
  }
}

export interface PicturesRelations {
  // describe navigational properties here
}

export type PicturesWithRelations = Pictures & PicturesRelations;
