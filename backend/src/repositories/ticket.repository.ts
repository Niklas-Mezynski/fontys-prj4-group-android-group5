import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Ticket, TicketRelations} from '../models';

export class TicketRepository extends DefaultCrudRepository<
  Ticket,
  typeof Ticket.prototype.id,
  TicketRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(Ticket, dataSource);
  }
}
