import {Entity, model, property} from '@loopback/repository';

@model()
export class User extends Entity {
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
  firstName: string;

  @property({
    type: 'string',
    required: true,
  })
  lastName: string;

  @property({
    type: 'string',
    required: true,
  })
  email: string;

  @property({
    type: 'string',
    required: true,
  })
  nick_name: string;

  @property({
    type: 'date',
    required: true,
  })
  birth_date: string;

  @property({
    type: 'string',
  })
  location?: string;

  @property({
    type: 'string',
  })
  profile_pic?: string;

  @property({
    type: 'string',
  })
  about_me?: string;

  @property({
    type: 'string',
    required: true,
  })
  salt: string;

  @property({
    type: 'string',
    required: true,
  })
  password: string;


  constructor(data?: Partial<User>) {
    super(data);
  }
}

export interface UserRelations {
  // describe navigational properties here
}

export type UserWithRelations = User & UserRelations;
