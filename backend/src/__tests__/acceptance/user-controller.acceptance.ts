import { User } from '../../models/user.model';
import {
    Client,
    createStubInstance,
    expect,
    sinon,
    StubbedInstanceWithSinonAccessor,
} from '@loopback/testlab';
import { ProjectX } from '../..';
import { setupApplication } from './test-helper';
import { UserLocationRepository, UserRepository } from '../../repositories';
import { UserController } from '../../controllers';
import { JWTService, MyUserService } from '@loopback/authentication-jwt';
import { SecurityBindings, securityId, UserProfile } from '@loopback/security';
import { authenticate, TokenService } from '@loopback/authentication';
import { givenEmptyDatabase } from '../helpers/database.helpers';
import { AnyType } from '@loopback/repository';

describe('UserController Test', () => {
    let userRepository: StubbedInstanceWithSinonAccessor<UserRepository>;
    let userLocationRepository: StubbedInstanceWithSinonAccessor<UserLocationRepository>;
    beforeEach(givenStubbedRepository);

    // your unit tests
    it('Test that a newly generated user becomes an ID', async () => {
        const controller = new UserController(
            sinon.createStubInstance(JWTService),
            sinon.createStubInstance(MyUserService),
            { [securityId]: "test123" },
            userRepository,
            userLocationRepository,
        )
        let newUser = new User({
            email: 'd@w1235342.com',
            password: '123456789',
            firstName: 'Dfweiopkf',
            lastName: 'Wfewfwfwe',
            nick_name: 'Dwfwewf',
            birth_date: '2002-04-19T09:51:19.229Z',
        });

        let res = await controller.create(newUser);
        sinon.assert.calledWithMatch(userRepository.stubs.create, sinon.match.has("id", sinon.match.string));
    });

    function givenStubbedRepository() {
        userRepository = createStubInstance(UserRepository);
        userLocationRepository = createStubInstance(UserLocationRepository);
    }
});
