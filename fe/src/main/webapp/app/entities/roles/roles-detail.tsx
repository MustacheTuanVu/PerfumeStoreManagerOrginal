import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './roles.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRolesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RolesDetail = (props: IRolesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { rolesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rolesDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.roles.detail.title">Roles</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rolesEntity.id}</dd>
          <dt>
            <span id="roleID">
              <Translate contentKey="perfumeStoreManagerApp.roles.roleID">Role ID</Translate>
            </span>
          </dt>
          <dd>{rolesEntity.roleID}</dd>
          <dt>
            <span id="roleName">
              <Translate contentKey="perfumeStoreManagerApp.roles.roleName">Role Name</Translate>
            </span>
          </dt>
          <dd>{rolesEntity.roleName}</dd>
        </dl>
        <Button tag={Link} to="/roles" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/roles/${rolesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ roles }: IRootState) => ({
  rolesEntity: roles.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RolesDetail);
