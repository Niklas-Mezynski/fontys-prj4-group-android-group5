import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Pictures, PicturesRelations} from '../models';

export class PicturesRepository extends DefaultCrudRepository<
  Pictures,
  typeof Pictures.prototype.event_id,
  PicturesRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(Pictures, dataSource);
  }
}
