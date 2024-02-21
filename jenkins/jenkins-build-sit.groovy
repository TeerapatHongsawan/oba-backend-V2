library 'lib-build-jenkinsk8s-docker@v5.5.0-fix_nexus_creds'
k8s_pod_build(
        pod_type: 'docker',
        pod_yaml: get_pod_template('docker', '19.03.1'),
        build_path: './',
        overide_command: 'docker build',
        email_receiver: 'warakorn.luangskulpong@scb.co.th',

        //*************Artifact/ Image Registry***********************
        registry_creds_id: 'harbor_nonprod',
        docker_registry: [
                url: 'harbordev.se.scb.co.th',
                image_name: 'ap2131-reportengine/oba-svc-backend-core-ms',
//                image_version: '1.0.0-SNAPSHOT'
        ],
 //       env_variable: [
 //               KEY_TEST_1: 'TestValue1',
 //               KEY_TEST_2: 'TestValue2'
 //       ],
)
