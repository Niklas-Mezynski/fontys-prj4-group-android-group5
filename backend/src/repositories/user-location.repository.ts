import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {UserLocation, UserLocationRelations, User} from '../models';
import {UserRepository} from './user.repository';

export class UserLocationRepository extends DefaultCrudRepository<
  UserLocation,
  typeof UserLocation.prototype.user_id,
  UserLocationRelations
> {

  public readonly user: BelongsToAccessor<User, typeof UserLocation.prototype.user_id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('UserRepository') protected userRepositoryGetter: Getter<UserRepository>,
  ) {
    super(UserLocation, dataSource);
    this.user = this.createBelongsToAccessorFor('user', userRepositoryGetter,);
    this.registerInclusionResolver('user', this.user.inclusionResolver);
  }
}
