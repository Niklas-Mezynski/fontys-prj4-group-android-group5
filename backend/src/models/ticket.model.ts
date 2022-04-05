import {Entity, model, property} from '@loopback/repository';

@model()
export class Ticket extends Entity {
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
  event_id: string;

  @property({
    type: 'string',
    required: true,
  })
  user_id: string;


  constructor(data?: Partial<Ticket>) {
    super(data);
  }
}

export interface TicketRelations {
  // describe navigational properties here
}

export type TicketWithRelations = Ticket & TicketRelations;
