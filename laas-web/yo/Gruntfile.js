// Generated on 2015-02-15 using
// generator-webapp 0.5.1
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// If you want to recursively match all subfolders, use:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Configurable paths
  var config = {
    app: 'app',
    dist: 'dist',
    templates : 'app/handlebars'
  };

  var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest ;

  var singlePage = function(req, res, next) {
    var url = req.url;
    console.log("url is ************* " + url);
    if (/.*\.js/.test(url) || /.*\.css/.test(url) || /.*fonts\/icons\./.test(url) || (/.*\/images\//.test(url))) {
      return next();
    }
    if (/^\/tasks/.test(url) || /^\/login/.test(url) || /^\/user/.test(url) || /^\/workflows/.test(url)|| /^\/home/.test(url) ||/^\/job/.test(url)) {
      req.url = '/index.html';
    }
    return next();
  };

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    config: config,

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['wiredep']
      },
      js: {
        files: ['<%= config.app %>/scripts/{,*/}*.js'],
        tasks: ['jshint'],
        options: {
          livereload: true
        }
      },
      jstest: {
        files: ['test/spec/{,*/}*.js'],
        tasks: ['test:watch']
      },
      jade: {
        files: [
          '<%= config.app %>/lib/{,*/}*.jade','<%= config.app %>/index.jade'
        ],
        tasks: ['jade:compile']
      },
      jade_handlebars : {
        files: ['<%= config.templates %>/**/*.jade'],
        tasks: ['jade_handlebars']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      less: {
        files: [
          '<%= config.app %>/styles/{,*/}*.less',
          '<%= config.app %>/styles/{,*/}*.overrides',
          '<%= config.app %>/styles/{,*/}*.variables'
        ],
        tasks: ['less:server', 'autoprefixer']
      },
      styles: {
        files: ['<%= config.app %>/styles/{,*/}*.css'],
        tasks: ['newer:copy:styles', 'autoprefixer']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= config.app %>/images/{,*/}*',
          '.tmp/{,*/}*.html',
          '.tmp/{,*/}*.js',
          '.tmp/styles/{,*/}*.css'
        ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9000,
        open: true,
        livereload: 35729,
        // Change this to '0.0.0.0' to access the server from outside
        hostname: 'localhost'
      },
      proxies: [{
        context: ['/api/v1','/controllers'],
        host: 'localhost',
        port: 8080,
        https: false,
        changeOrigin: true
      }],
      livereload: {
        options: {
          middleware: function(connect) {
            return [
              proxySnippet,
              singlePage,
              connect.static('.tmp'),
              connect().use('/bower_components', connect.static('./bower_components')),
              connect.static(config.app)
            ];
          }
        }
      },
      test: {
        options: {
          open: false,
          port: 9001,
          middleware: function(connect) {
            return [
              connect.static('test'),
              connect.static('.tmp'),
              connect().use('/bower_components', connect.static('./bower_components')),
              connect.static(config.app)
            ];
          }
        }
      },
      dist: {
        options: {
          base: '<%= config.dist %>',
          livereload: false
        }
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= config.dist %>/*',
            '!<%= config.dist %>/.git*'
          ]
        }]
      },
      server: '.tmp'
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: [
        'Gruntfile.js',
        '<%= config.app %>/scripts/{,*/}*.js',
        '!<%= config.app %>/scripts/vendor/*',
        'test/spec/{,*/}*.js'
      ]
    },

    // Mocha testing framework configuration options
    mocha: {
      all: {
        options: {
          run: true,
          urls: ['http://<%= connect.test.options.hostname %>:<%= connect.test.options.port %>/test.html']
        }
      }
    },

    less: {
      options: {
        paths: ['./bower_components']
      },
      dist: {
        options: {
          cleancss: true,
          report: 'gzip'
        },
        files: [{
          expand: true,
          cwd: '<%= config.app %>/styles',
          src: ['*.less', '!theme.less'],
          dest: '.tmp/styles',
          ext: '.css'
        }]
      },
      server: {
        options: {
          sourceMap: true,
          sourceMapBasepath: '<%= config.app %>/',
          sourceMapRootpath: '../'
        },
        files: [{
          expand: true,
          cwd: '<%= config.app %>/styles',
          src: ['*.less', '!theme.less'],
          dest: '.tmp/styles',
          ext: '.css'
        }]
      }
    },
    jade: {
      compile: {
        options: {
          client: false,
          pretty: true
        },
        files: [{
          expand: true,
          cwd: '<%= config.app %>',
          src: '*.jade',
          dest: '.tmp',
          ext: '.html'
        }]
      }
    },

    jade_handlebars: {
      all : {
        files : {
          '.tmp/handlebars/precompiled.handlebars.home.js': ['app/handlebars/**/*.jade']
        }
      }
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['> 1%', 'last 2 versions', 'Firefox ESR', 'Opera 12.1']
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the HTML file
    wiredep: {
      app: {
        ignorePath: /^\/|\.\.\//,
        src: [ '<%= config.app %>/index.jade' ]
      },
      less: {
        src: ['<%= config.app %>/styles/{,*/}*.less'],
        ignorePath: /(\.\.\/){1,2}bower_components\//
      }
    },

    // Renames files for browser caching purposes
    rev: {
      dist: {
        files: {
          src: [
            '<%= config.dist %>/scripts/{,*/}*.js',
            '<%= config.dist %>/styles/{,*/}*.css',
            '<%= config.dist %>/images/{,*/}*.*',
            '<%= config.dist %>/fonts/{,*/}*.*',
            '<%= config.dist %>/*.{ico,png}'
          ]
        }
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      options: {
        dest: '<%= config.dist %>'
      },
      html: '.tmp/*.html'
    },

    // Performs rewrites based on rev and the useminPrepare configuration
    usemin: {
      options: {
        assetsDirs: [
          '<%= config.dist %>',
          '<%= config.dist %>/images',
          '<%= config.dist %>/styles'
        ]
      },
      html: [
        '.tmp/{,*/}*.html'
      ],
      css: [
        '<%= config.dist %>/styles/{,*/}*.css',
        '.tmp/styles/{,*/}*.css'
      ]
    },

    // The following *-min tasks produce minified files in the dist folder

    htmlmin: {
      dist: {
        options: {
          collapseBooleanAttributes: true,
          collapseWhitespace: true,
          conservativeCollapse: true,
          removeAttributeQuotes: true,
          removeCommentsFromCDATA: true,
          removeEmptyAttributes: true,
          removeOptionalTags: true,
          removeRedundantAttributes: true,
          useShortDoctype: true
        },
        files: [{
          expand: true,
          cwd: '.tmp',
          src: '{,*/}*.html',
          dest: '<%= config.dist %>'
        }]
      }
    },

    // By default, your `index.html`'s <!-- Usemin block --> will take care
    // of minification. These next options are pre-configured if you do not
    // wish to use the Usemin blocks.
    // cssmin: {
    //   dist: {
    //     files: {
    //       '<%= config.dist %>/styles/main.css': [
    //         '.tmp/styles/{,*/}*.css',
    //         '<%= config.app %>/styles/{,*/}*.css'
    //       ]
    //     }
    //   }
    // },
    // uglify: {
    //   dist: {
    //     files: {
    //       '<%= config.dist %>/scripts/scripts.js': [
    //         '<%= config.dist %>/scripts/scripts.js'
    //       ]
    //     }
    //   }
    // },
    // concat: {
    //   dist: {}
    // },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= config.app %>',
          dest: '<%= config.dist %>',
          src: [
            '*.{ico,png,txt}',
            'images/{,*/}*.webp',
            '{,*/}*.html',
            'styles/fonts/{,*/}*.*',
            'themes/default/assets/{,*/}*.*',
            'public/{,*/}*.*'
          ]
        }, {
          src: 'node_modules/apache-server-configs/dist/.htaccess',
          dest: '<%= config.dist %>/.htaccess'
        }, {
          expand: true,
          cwd: 'bower_components/fontawesome',
          src: 'fonts/{,*/}*.*',
          dest: '<%= config.dist %>'
        }, {
          expand: true,
          cwd: 'bower_components/froala',
          src: 'fonts/{,*/}*.*',
          dest: '<%= config.dist %>'
        }]
      },
      styles: {
        expand: true,
        dot: true,
        cwd: '<%= config.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      }
    },

    // Generates a custom Modernizr build that includes only the tests you
    // reference in your app
    // modernizr: {
    //   dist: {
    //     devFile: 'bower_components/modernizr/modernizr.js',
    //     outputFile: '<%= config.dist %>/scripts/modernizr.js',
    //     files: {
    //       src: [
    //         '<%= config.dist %>/scripts/{,*/}*.js',
    //         '<%= config.dist %>/styles/{,*/}*.css',
    //         '!<%= config.dist %>/scripts/vendor/*'
    //       ]
    //     },
    //     uglify: true
    //   }
    // },

    // Run some tasks in parallel to speed up build process
    concurrent: {
      server: [
        'less:server',
        'copy:styles',
        'jade:compile'
      ],
      test: [
        'copy:styles'
      ],
      dist: [
        'less:dist',
        'copy:styles'
      ]
    }
  });


  grunt.registerTask('serve', 'start the server and preview your app, --allow-remote for remote access', function (target) {
    if (grunt.option('allow-remote')) {
      grunt.config.set('connect.options.hostname', '0.0.0.0');
    }
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'wiredep',
      'jade:compile',
      'jade_handlebars',
      'concurrent:server',
      'configureProxies:server',
      'autoprefixer',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run([target ? ('serve:' + target) : 'serve']);
  });

  grunt.registerTask('test', function (target) {
    if (target !== 'watch') {
      grunt.task.run([
        'clean:server',
        'concurrent:test',
        'autoprefixer'
      ]);
    }

    grunt.task.run([
      'connect:test',
      'mocha'
    ]);
  });

  grunt.registerTask('build', [
    'clean:dist',
    'wiredep',
    'jade:compile',
    'jade_handlebars',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'cssmin',
    'uglify',
    'copy:dist',
    // 'modernizr',
    'rev',
    'usemin',
    'htmlmin'
  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'test',
    'build'
  ]);
};
