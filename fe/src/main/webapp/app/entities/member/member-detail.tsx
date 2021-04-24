import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './member.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemberDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemberDetail = (props: IMemberDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memberEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memberDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.member.detail.title">Member</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.id}</dd>
          <dt>
            <span id="userID">
              <Translate contentKey="perfumeStoreManagerApp.member.userID">User ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.userID}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="perfumeStoreManagerApp.member.name">Name</Translate>
            </span>
          </dt>
          <dd>{memberEntity.name}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="perfumeStoreManagerApp.member.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{memberEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="perfumeStoreManagerApp.member.email">Email</Translate>
            </span>
          </dt>
          <dd>{memberEntity.email}</dd>
          <dt>
            <span id="roleID">
              <Translate contentKey="perfumeStoreManagerApp.member.roleID">Role ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.roleID}</dd>
          <dt>
            <span id="storeID">
              <Translate contentKey="perfumeStoreManagerApp.member.storeID">Store ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.storeID}</dd>
          <dt>
            <span id="salary">
              <Translate contentKey="perfumeStoreManagerApp.member.salary">Salary</Translate>
            </span>
          </dt>
          <dd>{memberEntity.salary}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.member.stores">Stores</Translate>
          </dt>
          <dd>{memberEntity.stores ? memberEntity.stores.id : ''}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.member.roles">Roles</Translate>
          </dt>
          <dd>{memberEntity.roles ? memberEntity.roles.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/member/${memberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ member }: IRootState) => ({
  memberEntity: member.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemberDetail);
