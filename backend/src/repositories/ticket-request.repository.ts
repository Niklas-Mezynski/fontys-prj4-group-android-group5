import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {TicketRequest, TicketRequestRelations, User} from '../models';
import {UserRepository} from './user.repository';
import {EventRepository} from './event.repository';

export class TicketRequestRepository extends DefaultCrudRepository<
  TicketRequest,
  typeof TicketRequest.prototype.user_id,
  typeof TicketRequest.prototype.event_id,
  TicketRequestRelations
> {

  public readonly TicketRequestToUser: BelongsToAccessor<User, typeof TicketRequest.prototype.user_id>;
  public readonly TicketRequestToEvent: BelongsToAccessor<Event, typeof TicketRequest.prototype.event_id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('UserRepository') protected userRepositoryGetter: Getter<UserRepository>, @repository.getter('EventRepository') protected eventRepositoryGetter: Getter<EventRepository>,
  ) {
    super(TicketRequest, dataSource);
    this.TicketRequestToUser = this.createBelongsToAccessorFor('TicketRequestToUser', userRepositoryGetter,);
    this.TicketRequestToEvent = this.createBelongsToAccessorFor('TicketRequestToEvent', eventRepositoryGetter,);
  }

  public findByUserId(user_id: string) {
    return this.findOne({where: {user_id}});
  }

  public findByEventId(event_id: string) {
    return this.findOne({where: {event_id}});
  }
}
