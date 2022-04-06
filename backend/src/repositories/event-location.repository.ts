import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {EventLocation, EventLocationRelations} from '../models';

export class EventLocationRepository extends DefaultCrudRepository<
  EventLocation,
  typeof EventLocation.prototype.event_id,
  EventLocationRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(EventLocation, dataSource);
  }
}
