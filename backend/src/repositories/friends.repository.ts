import {inject} from '@loopback/core';
import {AnyObject, DefaultCrudRepository} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Friends, FriendsRelations} from '../models';
import {securityId, UserProfile} from '@loopback/security';

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
    const sql = `SELECT * FROM getFriendInfos($1);`;
    return this.dataSource.execute(sql, [userId]);
  }

  getFriendByName(username: string): Promise<AnyObject> {
    const sql = `SELECT "user".id, "user".nick_name, "user".profile_pic, "user".about_me FROM "user" WHERE UPPER("user".nick_name) = $1;`;
    return this.dataSource.execute(sql, [username.toUpperCase()]);
  }

  deleteByIds(userId: string, friendId: string, currentUserProfile: UserProfile): Promise<void> {
    const sql = `
        DELETE FROM "friends"
        WHERE ("friends".usera = $1 AND "friends".userb = $2) OR
         ("friends".userb = $1 AND "friends".usera = $2) AND
        ("friends".usera = $3 OR "friends".userb = $3);`;
    return this.dataSource.execute(sql, [userId, friendId, currentUserProfile.id]);
  }
}
