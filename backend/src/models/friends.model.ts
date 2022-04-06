import {Entity, model, property} from '@loopback/repository';

@model()
export class Friends extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  userA: string;

  @property({
    type: 'string',
    id: true,
    generated: false,
    required: true,
  })
  userB: string;


  constructor(data?: Partial<Friends>) {
    super(data);
  }
}

export interface FriendsRelations {
  // describe navigational properties here
}

export type FriendsWithRelations = Friends & FriendsRelations;
