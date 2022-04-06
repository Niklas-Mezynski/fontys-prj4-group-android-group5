import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Friends, FriendsRelations} from '../models';

export class FriendsRepository extends DefaultCrudRepository<
  Friends,
  typeof Friends.prototype.userA,
  FriendsRelations
> {
  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource,
  ) {
    super(Friends, dataSource);
  }
}
