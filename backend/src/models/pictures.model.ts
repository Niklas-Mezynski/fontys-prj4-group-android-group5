import { Entity, model, property, belongsTo } from '@loopback/repository';
import { Event } from './event.model';

@model()
export class Pictures extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  img_uuid: string;

  @property({
    type: 'string',
    required: true,
  })
  base64: string;

  @property({
    type: 'boolean',
    required: false,
  })
  main_img: boolean;

  @belongsTo(() => Event, { name: 'event' })
  event_id: string;

  constructor(data?: Partial<Pictures>) {
    super(data);
  }
}

export interface PicturesRelations {
  // describe navigational properties here
}

export type PicturesWithRelations = Pictures & PicturesRelations;
