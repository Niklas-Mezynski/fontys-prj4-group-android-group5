import { inject } from '@loopback/core';
import { AnyObject, DefaultCrudRepository } from '@loopback/repository';
import { LocalDbDataSource } from '../datasources';
import { Friends, FriendsRelations } from '../models';

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

  getFriendsForUser(userId: string): Promise<AnyObject> {
    let sql: string = `SELECT * FROM getFriendInfos($1);`;
    return this.dataSource.execute(sql, [userId]);
  }
}
