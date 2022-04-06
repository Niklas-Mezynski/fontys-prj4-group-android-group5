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

  snens(userId:string, friendId:string) {
    let sql:string = 'SELECT "user".nick_name FROM ((SELECT f.usera as friendId FROM friends f where (userb = ?)) UNION (SELECT f.userb as friendId FROM friends f where (usera = ?))) AS friends INNER JOIN "user" ON "user".id = friends.friendId;';

    
  }

}
