import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Pictures, PicturesRelations, Event} from '../models';
import {EventRepository} from './event.repository';

export class PicturesRepository extends DefaultCrudRepository<
  Pictures,
  typeof Pictures.prototype.event_id,
  PicturesRelations
> {

  public readonly event: BelongsToAccessor<Event, typeof Pictures.prototype.event_id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('EventRepository') protected eventRepositoryGetter: Getter<EventRepository>,
  ) {
    super(Pictures, dataSource);
    this.event = this.createBelongsToAccessorFor('event', eventRepositoryGetter,);
    this.registerInclusionResolver('event', this.event.inclusionResolver);
  }
}
