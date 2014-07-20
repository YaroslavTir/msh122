environments {
    nfadin {
        server {
            port = 8080
            staticCfg {
                addStatic = true
                resourcePath = 'C:\\Work\\Proj\\MskMetro\\trunk\\MskMetro.Server\\static'
            }
        }
        images{
            pathToStore='c:\\Work\\Proj\\MskMetro\\trunk\\MskMetro.Server\\content\\images\\'
            baseUrl='http://localhost:81/images/'
            thumbwidth=300

            pathToStoreGenerated='c:\\Work\\Proj\\MskMetro\\trunk\\MskMetro.Server\\content\\images\\generated\\'
            baseUrlGenerated='http://localhost:81/images/generated/'
        }
    }
    pgrebenyuk {
        server {
            port = 8080
            staticCfg {
                addStatic = true
                resourcePath = 'C:\\Work\\MskMetro\\MskMetro.Server\\static'
            }
        }
        db {
            url = 'jdbc:postgresql://localhost:5432/metro1'
            username = 'metro'
            password = 'metro'
        }
        images{
            pathToStore='C:/Work/MskMetro/content/images/'
            baseUrl='http://localhost:81/images/'
            thumbwidth=100

            pathToStoreGenerated='C:/Work/MskMetro/content/images/generated/'
            baseUrlGenerated='http://localhost:83/images/generated/'

        }
    }
    test {
        db {
            url = 'jdbc:postgresql://localhost:5432/metro_test'
            username = 'metro'
            password = 'metro'
        }
        images{
            pathToStore='C:/Work/MskMetro/content/images/'
            baseUrl='http://localhost:81/images/'
            thumbwidth=100

            pathToStoreGenerated='C:/Work/MskMetro/content/images/generated/'
            baseUrlGenerated='http://localhost:81/images/generated/'
        }
    }
    mskmetrosrv01 {
        im {
            notification {
                delay = '10000'
            }
        }
        server {
            port = 8080
            staticCfg {
                addStatic = false
                resourcePath = '/'
            }
        }
        db {
            url = 'jdbc:postgresql://91.225.130.11:5432/metro'
            username = 'postgres'
            password = 'apassword'
        }
        common {
            validateImToken = true
            realIpHeader = 'X-Real-IP'
        }
    }
    apermyakov {
        server {
            port = 8080
            staticCfg {
                addStatic = true
                resourcePath = 'C:\\Work\\MskMetro\\MskMetro.Server\\static'
            }
        }
        db {
            url = 'jdbc:postgresql://localhost:5432/metro'
            username = 'postgres'
            password = 'postgres'
        }
        images{
            pathToStore='C:/Work/MskMetro/content/images/'
            baseUrl='http://apermyakov:81/images/'
            thumbwidth=100

            pathToStoreGenerated='C:/Work/MskMetro/content/images/generated/'
            baseUrlGenerated='http://localhost:81/images/generated/'
        }
        common {
            validateImToken = true
            realIpHeader = 'X-Real-IP'
        }
        im {
            notification {
                delay = '1000'
            }
        }
    }
    ymolodkov {
        server {
            port = 9091
            staticCfg {
                addStatic = true
                resourcePath = 'C:\\Work\\Proj\\MskMetro\\trunk\\MskMetro.Server\\static'
            }
        }
        images{
            pathToStore='c:\\Work\\Proj\\MskMetro\\trunk\\MskMetro.Server\\content\\images\\'
            baseUrl='http://localhost:81/images/'
            thumbwidth=300

            pathToStoreGenerated='c:\\Work\\Proj\\MskMetro\\trunk\\MskMe' +
                    'tro.Server\\content\\images\\generated\\'
            baseUrlGenerated='http://localhost:81/images/generated/'
        }
    }
}