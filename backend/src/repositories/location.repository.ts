import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Location, LocationRelations} from '../models';

export class LocationRepository extends DefaultCrudRepository<
  Location,
  typeof Location.prototype.id,
  LocationRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(Location, dataSource);
  }
}
