import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Event, EventRelations} from '../models';

export class EventRepository extends DefaultCrudRepository<
  Event,
  typeof Event.prototype.id,
  EventRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(Event, dataSource);
  }
}
