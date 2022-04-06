import { Entity, model, property, belongsTo } from '@loopback/repository';
import { type } from 'os';
import { User } from './user.model';

@model()
export class UserLocation extends Entity {

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

  @belongsTo(() => User, { name: 'user' }, {
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  user_id: string;

  constructor(data?: Partial<UserLocation>) {
    super(data);
  }
}

export interface UserLocationRelations {
  // describe navigational properties here
}

export type UserLocationWithRelations = UserLocation & UserLocationRelations;
